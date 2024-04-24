import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';

import Layout from './components/structural/Layout';
import Home from './components/content/Home';
import PageNotFound from './components/content/PageNotFound';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import RecipeSearchPage from './components/content/search-recipes/RecipeSearchPage';
import RecipePage from './components/content/recipe-page/RecipePage';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path='/' element={<Layout />}>
                    <Route index element={<Home />} />
                    <Route path='login' element={<Login />} />
                    <Route path='register' element={<Register />} />
                    <Route path='recipes' element={<RecipeSearchPage />} />
                    <Route path='recipes/:recipeId' element={<RecipePage />} />
                    <Route path='*' element={<PageNotFound />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
