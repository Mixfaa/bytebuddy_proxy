import com.mixfa.bytebuddy_proxy.ClassInstanceBuilder;
import com.mixfa.bytebuddy_proxy.MethodInterceptionDescription;
import com.mixfa.bytebuddy_proxy.ProxyClassMaker;
import model.Client;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


public class SimpleTest {
    private static String _receive;
    private static String _send;

    public static void receiveIntercepted(Client client, String message) {
        _receive = message;
    }

    @Test
    void test1() throws Throwable {

        var sendInterception = new MethodInterceptionDescription.Builder()
                .setMatcher(ElementMatchers.named("send"))
                .setImlp(MethodCall.run(() -> _send = "send")).build();

        var receiveInterception = new MethodInterceptionDescription.Builder()
                .setMatcher(ElementMatchers.named("receive"))
                .setImlp(MethodCall.invoke(
                                SimpleTest.class.getMethod("receiveIntercepted", Client.class, String.class))
                        .withThis()
                        .withAllArguments()
                ).build();


        var interceptions = List.of(sendInterception, receiveInterception);

        var proxyClass = ProxyClassMaker.makeProxyClass(Client.class, interceptions, (bb) -> bb.defineField("_field", String.class, Visibility.PUBLIC));
        var object = new ClassInstanceBuilder<>(proxyClass)
                .selectConstructor(int.class)
                .withField("_field","my little field")
                .newInstance(123);

        object.send("send");
        object.receive("receive");

        var fieldVal = object.getClass().getDeclaredField("_field").get(object);

        Assertions.assertTrue(fieldVal.equals("my little field") && _send.equals("send") && _receive.equals("receive"));
    }
}
