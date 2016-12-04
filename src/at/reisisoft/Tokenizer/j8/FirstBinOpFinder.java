package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.Token;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Created by Florian on 04.12.2016.
 */
public class FirstBinOpFinder implements BiConsumer<JavaAdvancedToken, Integer> {

    private final Map<JavaSimpleTokenType, Integer> map = new EnumMap<>(JavaSimpleTokenType.class);
    private final List<JavaSimpleTokenType> binOperators;

    public FirstBinOpFinder() {
        //This is a rather expensive operation -> do it only on init
        binOperators = Arrays.stream(JavaSimpleTokenType.values()).filter(JavaSimpleTokenType::isBinaryOperator).collect(Collectors.toList());
    }

    @Override
    public void accept(JavaAdvancedToken token, Integer index) {
        if (JavaAdvancedTokenType.BINARY_OPERATOR_RAW.equals(token.getTokenType())) {
            if (token.getChildren().size() != 1)
                throw new IllegalArgumentException(JavaAdvancedTokenType.BINARY_OPERATOR_RAW + " should only have one child");
            final Token<?, ?> token1 = token.getChildren().get(0);
            if (token1 instanceof JavaSimpleToken) {
                JavaSimpleTokenType jstt = ((JavaSimpleToken) token1).getTokenType();
                if (jstt.isBinaryOperator())
                    map.computeIfAbsent(jstt, (a) -> index);
            }
        }

    }

    public int getFirstIndex(JavaSimpleTokenType tokenType) {
        return map.getOrDefault(tokenType, -1);
    }

    public boolean anyBinopFound() {
        return binOperators.stream().mapToInt(this::getFirstIndex).anyMatch(i -> i >= 0);
    }

    public void clear() {
        map.clear();
    }
}
