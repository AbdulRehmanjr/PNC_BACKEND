package com.pnc.marketplace.utils.enums;

public enum ProductAllowed {

    STARTERSTALL(1),
    LOCALBAZAAR(10),
    EMPORIUMELITE(1000);

    private final int value;

    private ProductAllowed(int value) {
        this.value = value;
    }

    /**
     * The getValue() function returns the value of a variable.
     * 
     * @return The method is returning the value of the variable "value".
     */
    public int getValue() {
        return value;
    }
    
    /**
     * The function checks if a given product number is allowed to be updated based on its value.
     * 
     * @param productNumber The parameter `productNumber` is of type `ProductAllowed`.
     * @return The method is returning a boolean value.
     */
    public boolean canUpdate(ProductAllowed productNumber) {
        return productNumber.getValue() >= this.value;
    }
}
