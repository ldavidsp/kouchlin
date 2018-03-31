package org.kouchlin.test

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import org.junit.Test
import org.kouchlin.test.base.CouchDBBaseMockTest
import org.kouchlin.test.base.mock
import org.kouchlin.util.STATUS
import kotlin.test.assertTrue

class BasicCouchDBMockTest : CouchDBBaseMockTest() {

    @Test
    fun couchDBUpTestMockedTest() {

        val slot = FuelManager.instance.mock(200, "OK", "")

        assertTrue(couchdb.up())

        with(slot.captured) {
            assertTrue(method == Method.GET)
            assertTrue(path == "_up")
        }
    }

    @Test
    fun couchDBDownTestMockedTest() {

        val slot = FuelManager.instance.mock(404, "", "")

        assertTrue(!couchdb.up())

        with(slot.captured) {
            assertTrue(method == Method.GET)
            assertTrue(path == "_up")
        }
    }

    @Test
    fun existsDBMockedTest() {
        val slot = FuelManager.instance.mock(200)

        val database = couchdb.database("kouchlin-test-db")
        assertTrue(database.exists() == STATUS.OK)

        with(slot.captured) {
            assertTrue(method == Method.HEAD)
            assertTrue(path == "kouchlin-test-db")
        }
    }

    @Test
    fun notExistsDBMockedTest() {
        val slot = FuelManager.instance.mock(404)

        val database = couchdb.database("kouchlin-test-db")
        assertTrue(database.exists() == STATUS.NOT_FOUND)

        with(slot.captured) {
            assertTrue(method == Method.HEAD)
            assertTrue(path == "kouchlin-test-db")
        }
    }

    @Test
    fun compactMockTest() {
        val slot = FuelManager.instance.mock(202,
                json = "{ \"ok\": true }")
        val database = couchdb.database("kouchlin-test-db")
        assertTrue(database.compact())
        with(slot.captured) {
            assertTrue(method == Method.POST)
            assertTrue(path == "kouchlin-test-db/_compact")
        }
    }

    @Test
    fun compactFailMockTest() {
        val slot = FuelManager.instance.mock(statusCode = 404,
                json = "{ \"ok\": false }")
        val database = couchdb.database("kouchlin-test-db")
        assertTrue(!database.compact())
        with(slot.captured) {
            assertTrue(method == Method.POST)
            assertTrue(path == "kouchlin-test-db/_compact")
        }
    }

    @Test
    fun compactDDocMockTest() {
        val slot = FuelManager.instance.mock(statusCode = 202,
                json = "{ \"ok\": true }")
        val database = couchdb.database("kouchlin-test-db")
        assertTrue(database.compact("ddoc"))
        with(slot.captured) {
            assertTrue(method == Method.POST)
            assertTrue(path == "kouchlin-test-db/_compact/ddoc")
        }
    }

    @Test
    fun ensureFullCommitMockTest() {
        val slot = FuelManager.instance.mock(statusCode = 201,
                json = "{ \"ok\": true }")
        val database = couchdb.database("kouchlin-test-db")
        assertTrue(database.ensureFullCommit())
        with(slot.captured) {
            assertTrue(method == Method.POST)
            assertTrue(path == "kouchlin-test-db/_ensure_full_commit")
        }
    }

    @Test
    fun ensureFullCommitFailMockTest() {
        val slot = FuelManager.instance.mock(statusCode = 400,
                json = "{ \"ok\": false }")
        val database = couchdb.database("kouchlin-test-db")
        assert(!database.ensureFullCommit())
        with(slot.captured) {
            assert(method == Method.POST)
            assert(path == "kouchlin-test-db/_ensure_full_commit")
        }
    }

    @Test
    fun createDBMockTest() {
        val slot = FuelManager.instance.mock(statusCode = 201,
                json = "{ \"ok\": true }")
        val database = couchdb.database("kouchlin-test-db")
        assertTrue(database.create() == STATUS.CREATED)
        with(slot.captured) {
            assertTrue(method == Method.PUT)
            assertTrue(path == "kouchlin-test-db")
        }
    }

    @Test
    fun createDBFailMockTest() {
        val slot = FuelManager.instance.mock(statusCode = 400,
                json = "{ \"error\":\"invalid_db_name\",\"message\":\"invalid db name\" }")
        val database = couchdb.database("kouchlin-test-db")
        assertTrue(database.create() == STATUS.BAD_REQUEST)
        with(slot.captured) {
            assertTrue(method == Method.PUT)
            assertTrue(path == "kouchlin-test-db")
        }
    }
}