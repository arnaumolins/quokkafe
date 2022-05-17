package github.com.arnaumolins.quokkafe.Utils;

import androidx.navigation.NavDirections;

public interface TableItemAction {
    NavDirections navigate(String id);
}
