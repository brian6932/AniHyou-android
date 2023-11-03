package com.axiel7.anihyou.data.model.staff

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.axiel7.anihyou.R
import com.axiel7.anihyou.StaffDetailsQuery

@Composable
fun StaffDetailsQuery.Staff.yearsActiveFormatted(): String? {
    yearsActive?.getOrNull(0)?.let { startYear ->
        val possibleEndYear = yearsActive.getOrNull(1)
        return if (yearsActive.size > 1 && possibleEndYear != null) {
            "$startYear-$possibleEndYear"
        } else {
            "$startYear-${stringResource(R.string.present)}"
        }
    }
    return null
}

@Composable
fun String.staffRoleLocalized() = when (this) {
    "A&R Producer" -> stringResource(R.string.staff_role_a_and_r_producer)
    "ADR Director" -> stringResource(R.string.staff_role_adr_director)
    "Accessory Design" -> stringResource(R.string.staff_role_accessory_design)
    "Action Animator" -> stringResource(R.string.staff_role_action_animator)
    "Action Director" -> stringResource(R.string.staff_role_action_director)
    "Action Animation Director" -> stringResource(R.string.staff_role_action_animation_director)
    "Action Animation Supervisor" -> stringResource(R.string.staff_role_action_animation_supervisor)
    "Advertising" -> stringResource(R.string.staff_role_advertising)
    "Advertising Producer" -> stringResource(R.string.staff_role_advertising_producer)
    "Animation Director" -> stringResource(R.string.staff_role_animation_director)
    "Animation Producer" -> stringResource(R.string.staff_role_animation_producer)
    "Animation Production Assistance" -> stringResource(R.string.staff_role_animation_production_assistance)
    "Animation Supervisor" -> stringResource(R.string.staff_role_animation_supervisor)
    "Animation" -> stringResource(R.string.staff_role_animation)
    "Art" -> stringResource(R.string.staff_role_art)
    "Art Board" -> stringResource(R.string.staff_role_art_board)
    "Art Composition" -> stringResource(R.string.staff_role_accessory_design)
    else -> this
}