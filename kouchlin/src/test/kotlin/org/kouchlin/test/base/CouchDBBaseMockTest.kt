package org.kouchlin.test.base

import com.github.kittinunf.fuel.core.FuelManager
import org.junit.AfterClass
import org.junit.BeforeClass
import org.kouchlin.CouchDB

open class CouchDBBaseMockTest {
	companion object {
		lateinit var couchdb: CouchDB

		@BeforeClass
		@JvmStatic
		fun setUpClass() {
			couchdb = CouchDB("http://dummy-host:5984")
		}

		@AfterClass
		@JvmStatic
		fun teardown() {
            FuelManager.instance = FuelManager()
		}

	}
}