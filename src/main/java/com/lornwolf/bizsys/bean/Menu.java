package com.lornwolf.bizsys.bean;

public class Menu {
    private String parentMenuId;
    private String menuType;
    private String menuId;
    private String description;
    public String getParentMenuId() {
        return parentMenuId;
    }
    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }
    public String getMenuType() {
        return menuType;
    }
    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
