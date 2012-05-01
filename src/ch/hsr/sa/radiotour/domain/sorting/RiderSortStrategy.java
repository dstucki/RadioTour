package ch.hsr.sa.radiotour.domain.sorting;

import java.util.Comparator;

import ch.hsr.sa.radiotour.domain.BicycleRider;

public abstract class RiderSortStrategy implements Comparator<BicycleRider> {
	boolean ascending;

	public boolean isAscending() {
		return ascending;
	}

	public void switchAscending() {
		ascending = !(ascending);
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	@Override
	public abstract int compare(BicycleRider arg0, BicycleRider arg1);

	public static class SortByStartNr extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			return arg0.getStartNr() - arg1.getStartNr();
		}

	}

	public static class SortByName extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}

	}

	public static class SortByTeam extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			return arg0.getTeamShort().compareTo(arg1.getTeamShort());
		}

	}

	static class SortByCountry extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			return arg0.getCountry().compareTo(arg1.getCountry());
		}

	}

	public static class SortByVirtualRank extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			return arg0.getVirtual_deficit().compareTo(
					arg1.getVirtual_deficit());
		}

	}

	public static class SortByTimeBoni extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			// FIXME: TIME_BONI implement
			return arg0.getOfficial_time().compareTo(arg1.getOfficial_time());
		}

	}

	public static class SortByOfficialTime extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			return arg0.getOfficial_time().compareTo(arg1.getOfficial_time());
		}

	}

	public static class SortByOfficialHandicap extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			return arg0.getOfficial_deficit().compareTo(
					arg1.getOfficial_deficit());
		}

	}
}