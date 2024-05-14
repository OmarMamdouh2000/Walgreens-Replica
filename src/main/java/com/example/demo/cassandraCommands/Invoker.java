package com.example.demo.cassandraCommands;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

@Service
public class Invoker {

        private HashMap<String, String> commandMap;

        @Autowired
        private CategoriesRepo catRepo;
        @Autowired
        private ProductsRepo prodRepo;
        @Autowired
        private BrandsRepo brandRepo;

        public Invoker() {
                commandMap = new HashMap<>();
                commandMap.put("listCategoriesCommand", "com.example.demo.cassandraCommands.listCategoriesCommand");
                commandMap.put("getCategoryCommand", "com.example.demo.cassandraCommands.getCategoryCommand");
                commandMap.put("deleteCategoryCommand", "com.example.demo.cassandraCommands.deleteCategoryCommand");
                commandMap.put("addCategoryCommand", "com.example.demo.cassandraCommands.addCategoryCommand");
                commandMap.put("updateCategoryCommand", "com.example.demo.cassandraCommands.updateCategoryCommand");

                // Products
                commandMap.put("listProductsCommand", "com.example.demo.cassandraCommands.listProductsCommand");
                commandMap.put("getProductCommand", "com.example.demo.cassandraCommands.getProductCommand");
                commandMap.put("deleteProductCommand", "com.example.demo.cassandraCommands.deleteProductCommand");
                commandMap.put("addProductCommand", "com.example.demo.cassandraCommands.addProductCommand");
                commandMap.put("updateProductCommand", "com.example.demo.cassandraCommands.updateProductCommand");
                commandMap.put("listCategoryProductsCommand", "com.example.demo.cassandraCommands.listCategoryProductsCommand");
                commandMap.put("listBrandCommand", "com.example.demo.cassandraCommands.listBrandCommand");
                commandMap.put("getBrandCommand", "com.example.demo.cassandraCommands.getBrandCommand");
                commandMap.put("getBrandProductsCommand", "com.example.demo.cassandraCommands.getBrandProductsCommand");
                commandMap.put("deleteBrandCommand", "com.example.demo.cassandraCommands.deleteBrandCommand");
                commandMap.put("addBrandCommand", "com.example.demo.cassandraCommands.addBrandCommand");
                commandMap.put("updateBrandCommand", "com.example.demo.cassandraCommands.updateBrandCommand");
        }

        public Object executeCommand(String commandName, Object parameter, Map<String, Object> body) {
                if (commandMap.containsKey(commandName)) {
                        try {
                                if (body == null) {
                                        body = new HashMap<String, Object>();
                                }
                                if (parameter != null) {
                                        body.put("parameter", parameter);
                                }
                                String className = commandMap.get(commandName);
                                Class<?> class1 = Class.forName(className);
                                Constructor<?> constructor = class1.getDeclaredConstructor(CategoriesRepo.class,
                                                ProductsRepo.class, BrandsRepo.class); // replace with your parameter
                                                                                       // types
                                Object instance = constructor.newInstance(catRepo, prodRepo, brandRepo); // replace with
                                                                                                         // your actual
                                                                                                         // parameters

                                // If your class has a method you want to invoke, you can do so like this:
                                String methodName = "execute"; // replace with your method name
                                Method method = class1.getDeclaredMethod(methodName, Map.class); // replace with your
                                                                                                 // method parameters
                                return method.invoke(instance, body);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                }
                return "Command not found";
        }
}
