/**
 * WSCheckTransLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.opencps.paymentmgt.vtcpay.wssoap;

public class WSCheckTransLocator extends org.apache.axis.client.Service implements org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTrans {

    public WSCheckTransLocator() {
    }


    public WSCheckTransLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSCheckTransLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSCheckTransSoap
    private java.lang.String WSCheckTransSoap_address = "http://sandbox1.vtcebank.vn/pay.vtc.vn/gate/WSCheckTrans.asmx";

    public java.lang.String getWSCheckTransSoapAddress() {
        return WSCheckTransSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSCheckTransSoapWSDDServiceName = "WSCheckTransSoap";

    public java.lang.String getWSCheckTransSoapWSDDServiceName() {
        return WSCheckTransSoapWSDDServiceName;
    }

    public void setWSCheckTransSoapWSDDServiceName(java.lang.String name) {
        WSCheckTransSoapWSDDServiceName = name;
    }

    public org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoap getWSCheckTransSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSCheckTransSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSCheckTransSoap(endpoint);
    }

    public org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoap getWSCheckTransSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoapStub _stub = new org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoapStub(portAddress, this);
            _stub.setPortName(getWSCheckTransSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSCheckTransSoapEndpointAddress(java.lang.String address) {
        WSCheckTransSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoapStub _stub = new org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoapStub(new java.net.URL(WSCheckTransSoap_address), this);
                _stub.setPortName(getWSCheckTransSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WSCheckTransSoap".equals(inputPortName)) {
            return getWSCheckTransSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "WSCheckTrans");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "WSCheckTransSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSCheckTransSoap".equals(portName)) {
            setWSCheckTransSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
