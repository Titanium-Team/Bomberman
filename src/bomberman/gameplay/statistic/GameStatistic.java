package bomberman.gameplay.statistic;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class GameStatistic {

	private final Map<Statistic, Double> values = new LinkedHashMap<>();

	public GameStatistic() {

		Stream.of(Statistics.values()).forEach(e -> this.values.put(e, e.defaultValue()));

	}

	public double get(Statistic key) {
		return this.values.get(key);
	}

	public void update(Statistic key, double value) {
		if (this.values.containsKey(key)) {
			if (key.isMax()) {
				this.values.put(key, Math.max(this.values.get(key), value));
			} else if (key.isAdd()) {
				this.values.put(key, this.values.get(key) + value);
			} else {
				throw new IllegalStateException();
			}
		} else {
			this.values.put(key, value);
		}
	}

	@Override
	public String toString() {
		return String.format("{values: %s}", this.values.toString());
	}

}
