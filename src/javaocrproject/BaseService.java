/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

/**
 *
 * @author Xiang
 */
public class BaseService {
    // Private attribute to store the name of the service.
    // 用于存储服务名称的私有属性。
    private String serviceName;
    public BaseService(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceName() {
        return "Service Name: " + serviceName;
    }
}
