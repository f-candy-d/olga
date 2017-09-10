package com.f_candy_d.dutils;

/**
 * Created by daichi on 17/08/15.
 *
 * USAGE:: In a class which implements Quantizable interface...
 *
 * <pre>
 *     public enum MrChildren implements Quantizable {
 *
 *         SAKURAI_KAZUTOSHI(38),
 *         SUZUKI_HIDEYA(1114),
 *         NAKAGAWA_KEISUKE(826),
 *         TAHARA_KENICHI(924);
 *
 *         private final mQuantity;
 *
 *         public MrChildren(int quantity) {
 *             mQuantity = quantity;
 *         }
 *
 *         public static final Quantizable.Converter<\RequestService> CONVERTER =
 *             new Converter<\RequestService>() {
 *                 \@Override
 *                 public RequestService convertFromQuantity(int quantity) {
 *                         switch(quantity) {
 *                              case 38: return SAKURAI_KAZUTOSHI;
 *                              case 1114: return SUZUKI_HIDEYA;
 *                              case 826: return NAKAGAWA_KEISUKE;
 *                              case 924: return TAHARA_KENICHI;
 *                              default return null;
 *                         }
 *                    }
 *                 };
 *
 *         \@Override
 *         public int quantize() {
 *             return mQuantity;
 *         }
 *     }
 * </pre>
 *
 * Quantize any object using quantize() method.
 * To convert to a original object, use Quantizable.Converter<\T> like:
 * <pre>
 *     public static <\T> T dequantize(@NonNull Quantizable.Converter<\T> converter, int quantity) {
 *         return converter.convertFromQuantity(quantity);
 *     }
 * </pre>
 *
 * OR, only for enum class, it is possible to do by using {@link QuantizableHelper#convertFromEnumClass(Class, int)}.
 * In this case, static field 'CONVERTER' is not necessary.
 */

public interface Quantizable {

    int quantize();

    interface Converter<T> {

        T convertFromQuantity(int quantity);
    }
}