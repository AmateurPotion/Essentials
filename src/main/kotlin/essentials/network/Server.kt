package essentials.network

import arc.struct.Array
import essentials.Main
import essentials.Main.Companion.configs
import essentials.internal.Bundle
import essentials.internal.CrashReport
import essentials.internal.Log
import essentials.internal.PluginException
import mindustry.Vars
import org.hjson.JsonArray
import org.hjson.JsonObject
import org.hjson.JsonValue
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class Server : Runnable {
    var list = Array<Service?>()
    lateinit var serverSocket: ServerSocket
    var bundle = Bundle()
    fun shutdown() {
        try {
            Thread.currentThread().interrupt()
            serverSocket.close()
        } catch (e: IOException) {
            CrashReport(e)
        }
    }

    override fun run() {
        try {
            serverSocket = ServerSocket(configs.serverPort)
            Log.info("server.enabled")
            while (!serverSocket.isClosed) {
                val socket = serverSocket.accept()
                try {
                    val service = Service(socket)
                    service.start()
                    list.add(service)
                } catch (ignored: PluginException) {
                    // if key is null
                }
            }
        } catch (e: IOException) {
            if (!e.message.equals("socket closed", ignoreCase = true)) {
                CrashReport(e)
            }
            Thread.currentThread().interrupt()
        }
    }

    internal enum class Request {
        Ping, BanSync, Chat, Exit, UnbanIP, UnbanID, DataShare, CheckBan
    }

    inner class Service(var socket: Socket) : Thread() {
        var br: BufferedReader
        var os: DataOutputStream
        var spec: SecretKey
        var ip: String

        fun shutdown(bundle: String?, vararg parameter: String?) {
            try {
                os.close()
                br.close()
                socket.close()
                list.remove(this)
                if (bundle != null) Log.server(bundle, *parameter)
            } catch (ignored: Exception) {
            }
        }

        override fun run() {
            try {
                while (!currentThread().isInterrupted) {
                    currentThread().name = "$ip Client Thread"
                    ip = socket.inetAddress.toString().replace("/", "")
                    val value = Main.tool.decrypt(br.readLine(), spec)
                    val answer = JsonObject()
                    val data = JsonValue.readJSON(value).asObject()
                    when (Request.valueOf(data["type"].asString())) {
                        Request.Ping -> {
                            val msg = arrayOf("Hi $ip! Your connection is successful!", "Hello $ip! I'm server!", "Welcome to the server $ip!")
                            val rnd = SecureRandom().nextInt(msg.size)
                            answer.add("result", msg[rnd])
                            os.writeBytes(Main.tool.encrypt(answer.toString(), spec).trimIndent())
                            os.flush()
                            Log.server("client.connected", ip)
                        }
                        Request.BanSync -> {
                            Log.server("client.request.banlist", ip)

                            // 적용
                            val ban = data["ban"].asArray()
                            val ipban = data["ipban"].asArray()
                            val subban = data["subban"].asArray()
                            for (b in ban) {
                                Vars.netServer.admins.banPlayerID(b.asString())
                            }
                            for (b in ipban) {
                                Vars.netServer.admins.banPlayerIP(b.asString())
                            }
                            for (b in subban) {
                                Vars.netServer.admins.addSubnetBan(b.asString())
                            }

                            // 가져오기
                            val bans = JsonArray()
                            val ipbans = JsonArray()
                            val subbans = JsonArray()
                            for (b in Vars.netServer.admins.banned) {
                                bans.add(b.id)
                            }
                            for (b in Vars.netServer.admins.bannedIPs) {
                                ipbans.add(b)
                            }
                            for (b in Vars.netServer.admins.subnetBans) {
                                subbans.add(b)
                            }
                            answer.add("type", "bansync")
                            answer.add("ban", ban)
                            answer.add("ipban", ipban)
                            answer.add("subban", subban)
                            for (ser in list) {
                                val remoteip = ser!!.socket.inetAddress.toString().replace("/", "")
                                for (b in configs.banTrust) {
                                    if (b.asString() == remoteip) {
                                        ser.os.writeBytes(Main.tool.encrypt(answer.toString(), ser.spec).trimIndent())
                                        ser.os.flush()
                                        Log.server("server.data-sented", ser.socket.inetAddress.toString())
                                    }
                                }
                            }
                        }
                        Request.Chat -> {
                            val message = data["message"].asString()
                            for (p in Vars.playerGroup) {
                                p.sendMessage(if (p.isAdmin) "[#C77E36][$ip][RC] $message" else "[#C77E36][RC] $message")
                            }
                            for (ser in list) {
                                if (ser!!.spec !== spec) {
                                    ser!!.os.writeBytes(Main.tool.encrypt(value, ser.spec).trimIndent())
                                    ser.os.flush()
                                }
                            }
                            Log.server("server-message-received", ip, message)
                        }
                        Request.Exit -> {
                            shutdown("client.disconnected", ip, bundle["client.disconnected.reason.exit"])
                            interrupt()
                            return
                        }
                        Request.UnbanIP -> Vars.netServer.admins.unbanPlayerIP(data["ip"].asString())
                        Request.UnbanID -> Vars.netServer.admins.unbanPlayerID(data["uuid"].asString())
                        Request.DataShare -> {
                        }
                        Request.CheckBan -> {
                            var found = false
                            val uuid = data["target_uuid"].asString()
                            val ip = data["target_ip"].asString()
                            for (info in Vars.netServer.admins.banned) {
                                if (info.id == uuid) {
                                    found = true
                                    break
                                }
                            }
                            for (info in Vars.netServer.admins.bannedIPs) {
                                if (info == ip) {
                                    found = true
                                    break
                                }
                            }
                            answer.add("result", if (found) "true" else "false")
                            os.writeBytes(Main.tool.encrypt(answer.toString(), spec).trimIndent())
                            os.flush()
                        }
                    }
                }
                shutdown(null)
            } catch (e: IOException) {
                if (e.message != "Stream closed") CrashReport(e)
            } catch (e: Exception) {
                Log.server("client.disconnected", ip, bundle["client.disconnected.reason.error"])
            }
        }

        init {
            ip = socket.inetAddress.toString()
            br = BufferedReader(InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))
            os = DataOutputStream(socket.getOutputStream())

            // 키 값 읽기
            val authkey = br.readLine() ?: throw PluginException("Auth key is null")
            spec = SecretKeySpec(Base64.getDecoder().decode(authkey), "AES")
        }
    }
}