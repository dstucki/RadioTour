package ch.hsr.sa.radiotour.domain.sorting;

import java.util.ArrayList;
import java.util.Comparator;

import ch.hsr.sa.radiotour.domain.SpecialPointHolder;

public class ArrayListSpecialHolderComparator implements
		Comparator<ArrayList<SpecialPointHolder>> {

	@Override
	public int compare(ArrayList<SpecialPointHolder> object1,
			ArrayList<SpecialPointHolder> object2) {
		int bonuspoints1 = 0;
		int bonuspoints2 = 0;
		int bonustime1 = 0;
		int bonustime2 = 0;

		for (SpecialPointHolder holder : object1) {
			bonuspoints1 += holder.getPointBoni();
			bonustime1 += holder.getTimeBoni();
		}
		for (SpecialPointHolder holder : object2) {
			bonuspoints2 += holder.getPointBoni();
			bonustime2 += holder.getTimeBoni();
		}

		int result = bonustime2 - bonustime1;
		if (result == 0) {
			result = bonuspoints2 - bonuspoints1;
		}
		return result;
	}

}
