import java.time.LocalDate;

class Solution {

    public static Product convertProductDTOToProduct(ProductDTO dto) {

        return new Product(dto.getId(), dto.getModel(), dto.getPrice(), LocalDate.of(2023,1,15),"SuperVendor");
    }
}