package org.opencps.paymentmgt.vtcpay.wssoap;

public class WSCheckTransSoapProxy implements org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoap {
  private String _endpoint = null;
  private org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoap wSCheckTransSoap = null;
  
  public WSCheckTransSoapProxy() {
    _initWSCheckTransSoapProxy();
  }
  
  public WSCheckTransSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSCheckTransSoapProxy();
  }
  
  private void _initWSCheckTransSoapProxy() {
    try {
      wSCheckTransSoap = (new org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransLocator()).getWSCheckTransSoap();
      if (wSCheckTransSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSCheckTransSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSCheckTransSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSCheckTransSoap != null)
      ((javax.xml.rpc.Stub)wSCheckTransSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.opencps.paymentmgt.vtcpay.wssoap.WSCheckTransSoap getWSCheckTransSoap() {
    if (wSCheckTransSoap == null)
      _initWSCheckTransSoapProxy();
    return wSCheckTransSoap;
  }
  
  public java.lang.String checkPartnerTransation(int website_id, java.lang.String order_code, java.lang.String receiver_acc, java.lang.String sign) throws java.rmi.RemoteException{
    if (wSCheckTransSoap == null)
      _initWSCheckTransSoapProxy();
    return wSCheckTransSoap.checkPartnerTransation(website_id, order_code, receiver_acc, sign);
  }
  
  
}