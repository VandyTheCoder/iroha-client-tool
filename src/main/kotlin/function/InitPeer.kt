package me.lucifer.function

import io.grpc.ConnectivityState
import jp.co.soramitsu.iroha.java.IrohaAPI
import me.lucifer.common.Constant.IROHA_PEERS
import org.slf4j.LoggerFactory

object InitPeer {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun start() {
        log.info("Initializing PEERS connection ...")
        IROHA_PEERS.forEach { peer ->
            while (true) {
                when (val state = peer.channel.getState(true)) {
                    ConnectivityState.IDLE -> {
                        log.info("Connection to PEER[${peer.uri}] is idling")
                        Thread.sleep(500) // Wait for 0.5 seconds
                    }
                    ConnectivityState.CONNECTING -> {
                        log.info("Connecting to PEER[${peer.uri}] ...")
                        Thread.sleep(500) // Wait for 0.5 seconds
                    }
                    ConnectivityState.READY -> {
                        log.info("Connected to PEER[${peer.uri}] successfully!")
                        break
                    }
                    else -> {
                        throw RuntimeException("Unable to connect to PEER[${peer.uri}] with state: $state")
                    }
                }
            }
        }
        log.info("All PEERS are connected successfully!")
    }

    fun getPeer() : IrohaAPI = IROHA_PEERS.random()
}