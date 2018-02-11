package com.game.framework.base;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author liguorui
 * @date 2018/2/3 23:21
 */
@Component
public class CloseService implements ApplicationContextAware, InitializingBean {

    private Logger log = LoggerFactory.getLogger(CloseService.class);

    private ApplicationContext applicationContext;

    private List<ICloseEvent> closeEvents = new ArrayList<ICloseEvent>();

    public void onServerClose() {
        for (ICloseEvent event : this.closeEvents) {
            StopWatch sw = new StopWatch();
            sw.start();
            event.onServerClose();
            sw.stop();
            log.info("{} close cost {} ms!", event.getClass().getSimpleName(), sw.getTime());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ICloseEvent> beans = applicationContext.getBeansOfType(ICloseEvent.class);
        if (beans != null && beans.size() > 0) {
            Iterator<ICloseEvent> iterator = beans.values().iterator();
            while (iterator.hasNext()) {
                ICloseEvent closeEvent = iterator.next();
                closeEvents.add(closeEvent);
            }
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
