AJS.$(document).ready(() => {
	const initialize = () => {
		let $allowedProjectsSelect = AJS.$("#allowedProjectsSelect");
		$allowedProjectsSelect.auiSelect2();

		$allowedProjectsSelect.on("change", () => {
			AJS.$("#allowedProjects").val(AJS.$("#allowedProjectsSelect").val())
		})
	}

	initialize();
})