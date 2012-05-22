package ch.hsr.sa.radiotour.domain.sorting;

import java.util.Comparator;
import java.util.Date;

import ch.hsr.sa.radiotour.domain.RiderStageConnection;

public abstract class RiderSortStrategy implements
		Comparator<RiderStageConnection> {
	public static class SortByVirtualDeficit extends RiderSortStrategy {

		@Override
		public int compare(RiderStageConnection arg0, RiderStageConnection arg1) {
			if (arg0.getVirtualDeficit() == null) {
				arg0.setVirtualDeficit(new Date(0, 0, 0, 0, 0, 0));
			}
			if (arg1.getVirtualDeficit() == null) {
				arg1.setVirtualDeficit(new Date(0, 0, 0, 0, 0, 0));
			}

			return arg0.getVirtualDeficit().compareTo(arg1.getVirtualDeficit());
		}

	}

	boolean ascending;

	public boolean isAscending() {
		return ascending;
	}

	public void switchAscending() {
		ascending = !(ascending);
	}

	@Override
	public abstract int compare(RiderStageConnection arg0,
			RiderStageConnection arg1);

	public static class SortByStartNr extends RiderSortStrategy {
		@Override
		public int compare(RiderStageConnection arg0, RiderStageConnection arg1) {
			return arg0.getRider().getStartNr() - arg1.getRider().getStartNr();
		}

	}

	public static class SortByName extends RiderSortStrategy {
		@Override
		public int compare(RiderStageConnection arg0, RiderStageConnection arg1) {
			return arg0.getRider().getName()
					.compareTo(arg1.getRider().getName());
		}

	}

	public static class SortByTeam extends RiderSortStrategy {
		@Override
		public int compare(RiderStageConnection arg0, RiderStageConnection arg1) {
			return arg0.getRider().getTeamShort()
					.compareTo(arg1.getRider().getTeamShort());
		}

	}

	public static class SortByCountry extends RiderSortStrategy {
		@Override
		public int compare(RiderStageConnection arg0, RiderStageConnection arg1) {
			return arg0.getRider().getCountry()
					.compareTo(arg1.getRider().getCountry());
		}

	}

	public static class SortByVirtualRank extends RiderSortStrategy {
		@Override
		public int compare(RiderStageConnection arg0, RiderStageConnection arg1) {
			int temp = arg0.getVirtualDeficit().compareTo(
					arg1.getVirtualDeficit());
			if (temp != 0) {
				return temp;
			} else {
				return arg0.getRider().getStartNr()
						- arg1.getRider().getStartNr();
			}
		}

	}

	public static class SortByTimeBoni extends RiderSortStrategy {
		@Override
		public int compare(RiderStageConnection arg0, RiderStageConnection arg1) {
			// FIXME: TIME_BONI implement
			return arg0.getBonusTime() - arg1.getBonusTime();
		}
	}

	public static class SortByOfficialTime extends RiderSortStrategy {
		@Override
		public int compare(RiderStageConnection arg0, RiderStageConnection arg1) {
			return arg0.getOfficialTime().compareTo(arg1.getOfficialTime());
		}

	}

	public static class SortByOfficialHandicap extends RiderSortStrategy {
		@Override
		public int compare(RiderStageConnection arg0, RiderStageConnection arg1) {
			return arg0.getOfficialDeficit().compareTo(
					arg1.getOfficialDeficit());
		}

	}

}