import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';

import Layout from './components/structural/Layout';
import Home from './components/content/home/Home';
import PageNotFound from './components/content/PageNotFound';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import RecipeSearchPage from './components/content/recipe-search/RecipeSearchPage';
import RecipePage from './components/content/recipe-page/RecipePage';
import UserPantryPage from './components/content/user-pantry/UserPantryPage';
import UserFavoritesPage from './components/content/user-favorites/UserFavoritesPage';
import UserRecipesPage from './components/content/user-recipes/UserRecipesPage';
import UserSettingsPage from './components/content/user-settings/UserSettingsPage';

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
                    <Route path='user/pantry' element={<UserPantryPage />} />
                    <Route
                        path='user/favorites'
                        element={<UserFavoritesPage />}
                    />
                    <Route path='user/recipes' element={<UserRecipesPage />} />
                    <Route
                        path='user/settings'
                        element={<UserSettingsPage />}
                    />
                    <Route path='*' element={<PageNotFound />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
