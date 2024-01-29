package me.lucifer

import io.grpc.ConnectivityState
import jp.co.soramitsu.iroha.java.IrohaAPI
import javax.xml.bind.DatatypeConverter

class IrohaClientTool {
    companion object {
        private val irohaNodes = listOf("x.x.x.x:50051")
        private val irohaPeers = irohaNodes.map {
            val (host, port) = it.split(":")
            IrohaAPI(host, port.toInt())
        }


        @JvmStatic
        fun main(args: Array<String>) {
            val trxHash = "xxx"

            // Initialize Iroha peers
            println("IROHA_CLIENT :: Initializing Iroha peers ...")
            irohaPeers.forEach { peer ->
                while (true) {
                    when (val state = peer.channel.getState(true)) {
                        ConnectivityState.IDLE -> {
                            println("IROHA_CLIENT :: Connection to PEER[${peer.uri}] is idling ...")
                            Thread.sleep(500) // Wait for 0.5 seconds
                        }
                        ConnectivityState.READY -> {
                            println("IROHA_CLIENT :: Connected to PEER[${peer.uri}]!")
                            break
                        }
                        else -> {
                            throw RuntimeException("IROHA_CLIENT :: Unable to connect to iroha with state: $state")
                        }
                    }
                }
            }
            println("IROHA_CLIENT :: Done initializing Iroha peers!")

            // Get transaction status
            println("IROHA_CLIENT :: Getting transactopm with Hash[$trxHash] status ...")
            getTransactionStatus(listOf(DatatypeConverter.parseHexBinary(trxHash)))
        }

        @OptIn(ExperimentalStdlibApi::class)
        private fun getTransactionStatus(trxHashes : List<ByteArray>) {
            for (trxHash in trxHashes) {
                irohaPeers.forEach {
                    try {
                        val status = it.txStatusSync(trxHash)
                        println("IROHA_CLIENT :: PEER[${it.uri}] - Status of Transaction[${trxHash.toHexString()}] is ${status.txStatus.name}")
                    } catch (e: Exception) { e.printStackTrace() }
                }
            }
        }
    }
}