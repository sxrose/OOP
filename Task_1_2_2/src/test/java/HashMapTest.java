import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.sxrose1.maps.Map;


public class HashMapTest {
    @Test
    void basicTest() {
        Map<Integer, String> m =
                Map.<Integer, String>empty().insert(1, "One").insert(2, "Two").insert(3, "Three");

        Assertions.assertEquals(Optional.of("One"), m.find(1));
        Assertions.assertEquals(Optional.of("Two"), m.find(2));
        Assertions.assertEquals(Optional.of("Three"), m.find(3));

        Assertions.assertEquals(Optional.empty(), m.find(4));

        m.delete(1);
        Assertions.assertEquals(Optional.empty(), m.find(1));

        Assertions.assertThrows(NoSuchElementException.class, () -> m.deleteExcept(1));
    }

    @Test
    void largeTest() {
        Map<Integer, Integer> m = Map.empty();

        int upperBound = 8000;

        var range = IntStream.range(1, upperBound).toArray();

        for (var i : range) {
            m.insert(i, upperBound - i);
        }

        for (var i : range) {
            Assertions.assertEquals(Optional.of(upperBound - i), m.find(i));
        }

        Assertions.assertEquals(Optional.empty(), m.find(upperBound + 337));
        Assertions.assertEquals(Optional.empty(), m.find(-1));

        Assertions.assertThrows(NoSuchElementException.class, () -> m.deleteExcept(-1));

        for (var i : range) {
            m.deleteExcept(i);
        }

        for (var i : range) {
            Assertions.assertEquals(Optional.empty(), m.find(i));
        }
    }

    @Test
    void nonTrivialClassTest() {
        record Point(double x, double y) {}

        Map<Point, Double> m = Map.empty();

        final int sz = 64;
        var range1 = DoubleStream.iterate(0.42, (x) -> x + 0.5).limit(sz).toArray();
        var range2 = DoubleStream.iterate(1.0, (x) -> x + 1.69).limit(sz).toArray();

        for (var i : range1) {
            for (var j : range2) {
                m.insert(new Point(i, j), i + j);
            }
        }

        for (var i : range1) {
            for (var j : range2) {
                Assertions.assertEquals(Optional.of(i + j), m.find(new Point(i, j)));
                Assertions.assertEquals(
                        Optional.empty(), m.deleteExcept(new Point(i, j)).find(new Point(i, j)));
            }
        }
    }

    @Test
    void pathologicalTest() {
        class SillyInteger {
            final int val;

            SillyInteger(int x) {
                this.val = x;
            }

            @Override
            public boolean equals(Object other) {
                return other instanceof SillyInteger && ((SillyInteger) other).val == this.val;
            }

            @Override
            public int hashCode() {
                // we do a little trolling
                return 0;
            }
        }

        var range = IntStream.range(1, 100).toArray();

        Map<SillyInteger, Integer> m = Map.empty();

        for (var i : range) {
            Assertions.assertEquals(
                    Optional.of(i), m.insert(new SillyInteger(i), i).find(new SillyInteger(i)));
        }

        for (var i : range) {
            Assertions.assertEquals(
                    Optional.empty(),
                    m.deleteExcept(new SillyInteger(i)).find(new SillyInteger(i)));
        }
    }

    @Test
    void functorTest() {
        Map<Integer, String> m =
                Map.<Integer, String>empty().insert(1, "One").insert(2, "Two").insert(3, "Three");

        Assertions.assertEquals(
                Map.<Integer, Integer>empty().insert(1, 3).insert(2, 3).insert(3, 5),
                m.map(String::length));
    }
}
