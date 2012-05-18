package ch.hsr.sa.radiotour.domain.sorting;

import java.util.Comparator;
import java.util.Date;

import ch.hsr.sa.radiotour.domain.BicycleRider;

public abstract class RiderSortStrategy implements Comparator<BicycleRider> {
	public static class SortByVirtualDeficit extends RiderSortStrategy {

		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			if (arg0.getVirtual_deficit() == null) {
				arg0.setVirtual_deficit(new Date(0, 0, 0, 0, 0, 0));
			}
			if (arg1.getVirtual_deficit() == null) {
				arg1.setVirtual_deficit(new Date(0, 0, 0, 0, 0, 0));
			}

			return arg0.getVirtual_deficit().compareTo(
					arg1.getVirtual_deficit());
		}

	}

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

	public static class SortByCountry extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			return arg0.getCountry().compareTo(arg1.getCountry());
		}

	}

	public static class SortByVirtualRank extends RiderSortStrategy {
		@Override
		public int compare(BicycleRider arg0, BicycleRider arg1) {
			int temp = arg0.getVirtual_deficit().compareTo(
					arg1.getVirtual_deficit());
			if (temp != 0) {
				return temp;
			} else {
				return arg0.getStartNr() - arg1.getStartNr();
			}
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