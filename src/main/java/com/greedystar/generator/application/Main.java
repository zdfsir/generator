package com.greedystar.generator.application;

import com.greedystar.generator.invoker.Many2ManyInvoker;
import com.greedystar.generator.invoker.Many2OneInvoker;
import com.greedystar.generator.invoker.One2ManyInvoker;
import com.greedystar.generator.invoker.SingleInvoker;
import com.greedystar.generator.invoker.base.Invoker;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author GreedyStar
 * @since 2018/9/5
 */
public class Main {

    public static void main(String[] args) {
        single();
    }

    public static void single() {
        Map<String, String> invokerMap = new HashMap<>();
        invokerMap.put("account", "Account");
//        invokerMap.put("account_admin", "AccountAdmin");
//        invokerMap.put("account_group", "AccountGroup");
//        invokerMap.put("account_personal", "AccountPersonal");
//        invokerMap.put("account_referrer_relation", "AccountReferrerRelation");
//        invokerMap.put("account_referrer_relation_history", "AccountReferrerRelationHistory");
//        invokerMap.put("account_role_relation", "AccountRoleRelation");

        invokerMap.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String tableName, String className) {
                Invoker invoker = new SingleInvoker.Builder()
                        .setTableName(tableName)
                        .setClassName(className)
                        .build();
                invoker.execute();
            }
        });
    }

    public static void many2many() {
        Invoker invoker = new Many2ManyInvoker.Builder()
                .setTableName("user")
                .setClassName("User")
                .setParentTableName("role")
                .setParentClassName("Role")
                .setRelationTableName("user_role")
                .setForeignKey("user_id")
                .setParentForeignKey("role_id")
                .build();
        invoker.execute();
    }

    public static void many2one() {
        Invoker one2many = new Many2OneInvoker.Builder()
                .setTableName("user")
                .setClassName("User")
                .setParentTableName("office")
                .setParentClassName("Office")
                .setForeignKey("office_id")
                .build();
        one2many.execute();
    }

    public static void one2many() {
        Invoker invoker = new One2ManyInvoker.Builder()
                .setTableName("user")
                .setClassName("User")
                .setParentTableName("article")
                .setParentClassName("Article")
                .setParentForeignKey("user_id")
                .build();
        invoker.execute();
    }

}
