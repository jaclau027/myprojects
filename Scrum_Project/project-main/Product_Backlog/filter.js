document.addEventListener("DOMContentLoaded",function(){
    const tagCheckboxes = document.querySelectorAll(".tag-container input[type=checkbox]");
  
    // Attach a change event listener to each tag checkbox
    tagCheckboxes.forEach(function (checkbox) {
      checkbox.addEventListener("change", applyFilter);
    });
    function applyFilter() {
        const selectedTags = [];
  
        // Get all selected tag checkboxes
        const tagCheckboxes = document.querySelectorAll(".tag-container input[type=checkbox]:checked");
      
        // Extract values of selected tags
        tagCheckboxes.forEach(function (checkbox) {
          selectedTags.push(checkbox.value);
        });
        const cardViews = document.querySelectorAll(".cardview");
        cardViews.forEach(cardView => {
            const tags = cardView.querySelectorAll(".tags");
            const taskTags = []
            tags.forEach(tag =>{
                var pseudoElementContent = window.getComputedStyle(tag, '::before').content;
                pseudoElementContent = pseudoElementContent.replace(/['"]+/g, '');
                taskTags.push(pseudoElementContent)
        })
        const show = selectedTags.every(tag => taskTags.includes(tag))
        if (show){
            cardView.style.display = "block"
        }
        else {
            cardView.style.display = "none"
        }    
    })}
})