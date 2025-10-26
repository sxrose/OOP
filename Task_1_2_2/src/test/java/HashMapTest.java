import java.util.NoSuchElementException;
import java.util.Optional;
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

    for (var i : range) m.deleteExcept(i);

    for (var i : range) Assertions.assertEquals(Optional.empty(), m.find(i));
  }
}
