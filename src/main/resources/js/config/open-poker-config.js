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

	initialize();
});