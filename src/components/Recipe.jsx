function Recipe({ ingredients }) {
    return (
        <div className="page-content">
        <h1>WORK IN PROGRESS</h1>
        <h3>redo this later</h3>
        <ul>
          {ingredients.map((ingredient, index) => (
            <li key={index}>{ingredient}</li>
          ))}
        </ul>
      </div>
    );
  }
  
  export default Recipe;
  