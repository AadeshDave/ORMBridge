package com.test.annotations;
import com.orm.annotation.CompositeKey;
import com.orm.annotation.Check;
import com.orm.annotation.Column;
@CompositeKey(columns={"order_id","item_id"},table="order_item")
public class OrderItem implements java.io.Serializable,Comparable<OrderItem>
{
@Column(name="order_id")
private int orderId;
@Column(name="item_id")
private int itemId;
@Column(name="quantity")
@Check(constraint="quantity >= 1")
private int quantity;
public OrderItem()
{
this.orderId=0;
this.itemId=0;
this.quantity=0;
}
public OrderItem(int orderId,int itemId,int quantity)
{
this.orderId=orderId;
this.itemId=itemId;
this.quantity=quantity;
}
public void setOrderId(int orderId)
{
this.orderId=orderId;
}
public void setItemId(int itemId)
{
this.itemId=itemId;
}
public void setQuantity(int quantity)
{
this.quantity=quantity;
}
public int getOrderId()
{
return this.orderId;
}
public int getItemId()
{
return this.itemId;
}
public int getQuantity()
{
return this.quantity;
}
public boolean equals(Object object)
{
if(this==object) return true;
if(object==null || getClass()!=object.getClass()) return false;
OrderItem other=(OrderItem)object;
return this.orderId==other.orderId && this.itemId==other.itemId;
}
public int compareTo(OrderItem object)
{
OrderItem other=object;
int c3=Integer.compare(this.orderId,other.orderId);
int c4=Integer.compare(this.itemId,other.itemId);
if(c3!=0) return c3;
return c4;
}
public int hashCode()
{
return java.util.Objects.hash(orderId, itemId, quantity);
}
}
