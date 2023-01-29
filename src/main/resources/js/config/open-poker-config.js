AJS.$(document).ready(() => {
	const initialize = () => {
		let $allowedProjectsSelect = AJS.$("#allowedProjectsSelect");
		$allowedProjectsSelect.auiSelect2(
			{
				placeholder: "Open poker will be available for each project",
				allowClear:  true
			});

		$allowedProjectsSelect.on("change", () => {
			AJS.$("#allowedProjects").val(AJS.$("#allowedProjectsSelect").val());
		});
	};
//	This line disables "Changes you made may not be saved" pop-up window because it doesn't work properly
// TODO: that should be investigated
    window.onbeforeunload = null;
	initialize();
});
