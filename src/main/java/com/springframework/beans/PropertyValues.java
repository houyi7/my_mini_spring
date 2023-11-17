package com.springframework.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
    private final List<PropertyValue>propertyValueList= new ArrayList<>();

    public PropertyValue[]getPropertyValues(){return this.propertyValueList.toArray(propertyValueList.toArray(new PropertyValue[0]));}

    public void addPropertyValue(PropertyValue propertyValue){
        for(int i=0;i<propertyValueList.size();i++){
            if(propertyValueList.get(i).getName().equals(propertyValue.getName())){
                propertyValueList.set(i,propertyValue);
                return;
            }
        }
       propertyValueList.add(propertyValue);
    }
}
