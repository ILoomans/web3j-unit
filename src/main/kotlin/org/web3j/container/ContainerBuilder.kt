/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.container

import java.lang.RuntimeException
import org.web3j.NodeType
import org.web3j.abi.datatypes.Address
import org.web3j.container.besu.BesuContainer
import org.web3j.container.geth.GethContainer
import org.web3j.container.local.EmbeddedService
import org.web3j.evm.Configuration
import org.web3j.evm.PassthroughTracer

class ContainerBuilder {

    private lateinit var genesisPath: String
    private lateinit var type: NodeType
    private lateinit var selfAddress: String
    private var version: String? = null
    private val resourceFiles: HashMap<String, String> = hashMapOf()
    private val hostFiles: HashMap<String, String> = hashMapOf()

    fun type(type: NodeType) = apply {
        this.type = type
    }

    fun version(version: String?) = apply {
        this.version = version
    }

    fun withGenesis(genesisPath: String) = apply {
        this.genesisPath = genesisPath
    }

    fun withSelfAddress(selfAddress: String) = apply {
        this.selfAddress = selfAddress
    }

    fun build(): GenericService {
        return when (type) {
            NodeType.BESU -> BesuContainer(version, resourceFiles, hostFiles, genesisPath)
            NodeType.GETH -> GethContainer(version, resourceFiles, hostFiles, genesisPath)
            NodeType.EMBEDDED -> EmbeddedService(Configuration(Address(selfAddress), 10), PassthroughTracer())
            NodeType.PARITY -> throw RuntimeException("Container Type Not Supported: $type")
        }
    }
}
