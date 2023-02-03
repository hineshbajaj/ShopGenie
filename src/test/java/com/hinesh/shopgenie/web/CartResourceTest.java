package com.hinesh.shopgenie.web;

import com.hinesh.shopgenie.utils.TestContainerResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(TestContainerResource.class)
public class CartResourceTest {

    @Test
    void testSomeOperationOrFeature() {

    }
}
