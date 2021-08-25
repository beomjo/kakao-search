package io.github.beomjo.search

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SomeTestSpec : FunSpec({
    test("simple test") {
        "hello".length shouldBe 5
    }
})