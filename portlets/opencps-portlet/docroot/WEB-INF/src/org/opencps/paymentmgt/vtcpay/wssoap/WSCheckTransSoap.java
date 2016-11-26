/**
 * WSCheckTransSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.opencps.paymentmgt.vtcpay.wssoap;

public interface WSCheckTransSoap extends java.rmi.Remote {
    public java.lang.String checkPartnerTransation(int website_id, java.lang.String order_code, java.lang.String receiver_acc, java.lang.String sign) throws java.rmi.RemoteException;
}
