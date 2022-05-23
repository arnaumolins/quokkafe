package github.com.arnaumolins.quokkafe.utils;

import androidx.navigation.NavDirections;

public interface EventItemAction {

    NavDirections navigate(String id);
}
