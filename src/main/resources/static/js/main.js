var csrfToken = document
  .querySelector('meta[name="_csrf"]')
  .getAttribute("content");
var csrfHeader = document
  .querySelector('meta[name="_csrf_header"]')
  .getAttribute("content");

document.body.addEventListener("htmx:configRequest", function (evt) {
  evt.detail.parameters["auth_token"] = csrfToken; // add a new parameter into the request
  evt.detail.headers[csrfHeader] = csrfToken; // add a new header into the request
});

function deleteDrug(e) {
  let dataSet = e.dataset;

  let drugId = dataSet.drugId;
  let drugName = dataSet.drugName;
  let drugSeller = dataSet.drugSeller;

  Swal.fire({
    title: `Delete drug ${drugName} by ${drugSeller}?`,
    text: "You won't be able to revert this!",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Yes, delete it!",
  }).then((result) => {
    if (result.isConfirmed) {
      htmx
        .ajax("DELETE", "/admin/delete-drug/" + drugId, "#drugsTable")
        .then(() => {
          Swal.fire("Deleted!", "Drug has been deleted.", "success");
        });
    }
  });
}
