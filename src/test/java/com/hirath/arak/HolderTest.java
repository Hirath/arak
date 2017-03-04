package com.hirath.arak;

import static com.hirath.arak.HolderBuilder.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import com.hirath.arak.NoResponseForMethodException;

public class HolderTest {
	
	@Test
	public void build_WithStringSupplier_Succeed(){
		ITest object = as(ITest.class).call(()->"naming").when(response -> response.name()).instantiate();
		assertThat(object).extracting(ITest::name).hasOnlyElementsOfType(String.class)
		                                          .contains("naming");
	}
	
	@Test
	public void build_WithStringReply_Succeed(){
		ITest object = as(ITest.class).reply("naming").when(response -> response.name()).instantiate();
		assertThat(object).extracting(ITest::name).hasOnlyElementsOfType(String.class)
		                                          .contains("naming");
	}
	
	@Test
	public void build_WithIntegerSupplier_Succeed(){
		ITest object = as(ITest.class).call(()->10).when(response -> response.age()).instantiate();
		assertThat(object).extracting(ITest::age).hasOnlyElementsOfType(Integer.class)
		                                         .contains(10);
	}
	
	@Test
	public void build_WithIntegerReply_Succeed(){
		ITest object = as(ITest.class).reply(10).when(response -> response.age()).instantiate();
		assertThat(object).extracting(ITest::age).hasOnlyElementsOfType(Integer.class)
		                                         .contains(10);
	}
	
	@Test
	public void build_WithBooleanSupplier_Succeed(){
		ITest object = as(ITest.class).call(()->true).when(response -> response.adult()).instantiate();
		assertThat(object).extracting(ITest::adult).hasOnlyElementsOfType(Boolean.class)
		                                           .contains(true);
	}
	
	@Test
	public void build_WithBooleanReply_Succeed(){
		ITest object = as(ITest.class).reply(true).when(response -> response.adult()).instantiate();
		assertThat(object).extracting(ITest::adult).hasOnlyElementsOfType(Boolean.class)
		                                           .contains(true);
	}
	
	@Test
	public void build_WithDefault_Succeed(){
		ITest object1 = as(ITest.class).reply("naming").when(response -> response.name()).instantiate();
		ITest object2 = as(ITest.class).defautTo(object1).instantiate();

		assertThat(object2).extracting(ITest::name).hasOnlyElementsOfType(String.class)
		                                           .contains("naming");
	}
	
	@Test
	public void build_With2DefaultsImplemetingSameMethod_FirstDefaultInvoqued(){
		ITest object1 = as(ITest.class).reply("naming1").when(response -> response.name()).instantiate();
		ITest object2 = as(ITest.class).reply("naming2").when(response -> response.name()).instantiate();

		ITest object3 = as(ITest.class).defautTo(object1).then(object2).instantiate();

		assertThat(object3).extracting(ITest::name).hasOnlyElementsOfType(String.class)
		                                           .contains("naming1");
	}
	
	@Test
	public void build_WithNoResponse_NoResponseForMethodExceptionThrown(){
		ITest object = as(ITest.class).instantiate();
		Throwable thrown = catchThrowable(() -> object.name());
		assertThat(thrown).isInstanceOf(NoResponseForMethodException.class)
		                  .hasMessage(NoResponseForMethodException.MESSAGE_FORMAT, "name");
	}
}
