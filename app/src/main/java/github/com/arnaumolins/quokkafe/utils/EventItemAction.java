package github.com.arnaumolins.quokkafe.Utils;

import androidx.navigation.NavDirections;

public interface EventItemAction {
    NavDirections navigate(String id);
}
