package com.wsdc.g_a_0;

public class RouterUtil {
    public static RouterBean parse(String router){
        String[] split = router.split("/");
        RouterBean bean = new RouterBean();
        bean.module_name = split[1];
        bean.router_level_1 = "/" + split[2];
        if(split.length >= 4){
            bean.router_level_2 = "/" + split[3];
        }

        if(bean.router_level_2 != null){
            bean.router_name = bean.router_level_1 + bean.router_level_2;
            bean.level = 2;
        }else{
            bean.router_name = bean.router_level_1;
            bean.level = 1;
        }

        return bean;
    }


    public static class RouterBean{
        public String module_name;
        public String router_name;
        public String router_level_1;
        public String router_level_2;
        public int level;
    }
}
