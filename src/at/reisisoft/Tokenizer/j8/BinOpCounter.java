package at.reisisoft.Tokenizer.j8;

import at.reisisoft.Tokenizer.Token;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by Florian on 04.12.2016.
 */
public class BinOpCounter implements Consumer<JavaAdvancedToken> {

    private final Map<JavaSimpleTokenType, AtomicInteger> map = new EnumMap<>(JavaSimpleTokenType.class);
    //Do not increment this, we use it as 0
    private final AtomicInteger zero = new AtomicInteger();

    @Override
    public void accept(JavaAdvancedToken token) {
        if (JavaAdvancedTokenType.BINARY_OPERATOR_RAW.equals(token.getTokenType())) {
            if (token.getChildren().size() != 1)
                throw new IllegalArgumentException(JavaAdvancedTokenType.BINARY_OPERATOR_RAW + " should only have one child");
            final Token<?, ?> token1 = token.getChildren().get(0);
            if (token1 instanceof JavaSimpleToken) {
                JavaSimpleTokenType jstt = ((JavaSimpleToken) token1).getTokenType();
                if (jstt.isBinaryOperator())
                    map.computeIfAbsent(jstt, (a) -> new AtomicInteger()).incrementAndGet();
            }
        }

    }

    public int getCount(JavaSimpleTokenType tokenType) {
        return map.getOrDefault(tokenType, zero).get();
    }

    public void clear() {
        map.clear();
    }
}
