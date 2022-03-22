package com.blithe.crm.web.filter;

import com.blithe.crm.setting.domain.DicValue;
import com.blithe.crm.setting.service.DicService;
import com.blithe.crm.setting.service.impl.DicServiceImpl;

import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Author:  blithe.xwj
 * Date:    2022/3/21 18:30
 * Description:
 */

public class SysInitListener implements ServletContextListener {

   /**
    * 该方法是用来监听上下文域的方法，当服务器启动，上下文域对象创建，
    * 对象创建完毕后，马上执行该方法
    * @param sce 该参数能够取得监听的对象，监听的是什么对象，就可以通过
    *            该参数取得什么对象，例如我们现在监听的是上下文对象，通
    *            过该参数就可以取得上下文对象
    */
   @Override
   public void contextInitialized(ServletContextEvent sce) {
      ServletContext application = sce.getServletContext();
      DicService dicService = WebApplicationContextUtils.getWebApplicationContext(application).getBean(DicServiceImpl.class);



      // 取数据字典
      /*
         找业务层拿取7个List
            打包成为一个map
            业务层（像这样保存数据）：
               map.put("appellation",dataList1);
               map.put("clueStateList",dataList2);
               map.put("stageList",dataList3);
               ...

       */
      Map<String, List<DicValue>> map = dicService.selectAll();
      // 将map解析为上下文域对象中保存的键值对
      Set<String> keys =  map.keySet();
      for(String key : keys){
         application.setAttribute(key,map.get(key));
      }

      ServletContextListener.super.contextInitialized(sce);
   }

}
