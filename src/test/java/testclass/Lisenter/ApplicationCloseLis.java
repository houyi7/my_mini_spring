package testclass.Lisenter;

import com.springframework.context.ApplicationListener;
import com.springframework.context.event.ContextClosedEvent;

public class ApplicationCloseLis  implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println(this.getClass().getName());
    }
}
