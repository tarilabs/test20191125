package test20191125;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ModelTest {

    public static final Logger LOG = LoggerFactory.getLogger(ModelTest.class);

    @Test
    public void test() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        DMNRuntime dmnRuntime = KieRuntimeFactory.of(kieContainer.newKieBase(kieServices.newKieBaseConfiguration())).get(DMNRuntime.class);

        String namespace = "http://sample.dmn";
        String modelName = "MyDecision";

        DMNModel dmnModel = dmnRuntime.getModel(namespace, modelName);

        DMNContext context = dmnRuntime.newContext();
        context.set("MyInput", makeInput(1));
        DMNResult evaluateAll = dmnRuntime.evaluateAll(dmnModel, context);
        LOG.info("{}", evaluateAll);
        assertThat(evaluateAll.getDecisionResultByName("MyDecision").getResult(), is(true));

        context = dmnRuntime.newContext();
        context.set("MyInput", makeInput(999));
        evaluateAll = dmnRuntime.evaluateAll(dmnModel, context);
        LOG.info("{}", evaluateAll);
        assertThat(evaluateAll.getDecisionResultByName("MyDecision").getResult(), is(false));
    }

    private Map<String, Object> makeInput(int value) {
        Map<String, Object> myInput = new HashMap();
        myInput.put("myNumber", value);
        return myInput;
    }
}
