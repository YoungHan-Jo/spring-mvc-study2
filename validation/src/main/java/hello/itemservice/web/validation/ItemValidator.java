package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz); // 파라미터로 넘어오는 클래스가 Item 클래스에 해당하는지(자식들 포함)
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        //검증 로직
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemNamew", "required"); // hasText한것과 동일한 로직, 줄여쓰기
        if (!StringUtils.hasText(item.getItemName())) {
            /* 메세지 코드 부분에 new String[]() 배열로 여러개 하면 첫번째거 없으면 두번재거 찾음*/
            errors.rejectValue("itemName", "required");
//            new String[] ("required.item.itemName"), "required") 우선순위로 두개 만들어줌. 그래서 properties 에서 우선순위 적용가능함
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
    }
}
