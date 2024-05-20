export interface RecipeImage {
    id: number;
    url: string;
    altText: string;
}

export interface RecipeSearchResult {
    id: number;
    name: string;
    description: string;
    authorUsername: string;
    mainImage: RecipeImage;
    aggregateRating: number | null;
}

export interface Page<T> {
    content: Array<T>;
    pageable: {
        pageNumber: number;
        pageSize: number;
        sort: {
            empty: boolean;
            sorted: boolean;
            unsorted: boolean;
        };
        offset: number;
        paged: boolean;
        unpaged: boolean;
    };
    last: boolean;
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
    sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
    };
    first: boolean;
    numberOfElements: number;
    empty: boolean;
}

export interface User {
    username: string;
    email: string;
}

export interface AuthResponseData {
    msg: string;
    user: User;
    eat: number;
}

export interface RecipeIngredient {
    id: number;
    name: string;
    quantity: string;
}

export interface RecipeInstruction {
    id: number;
    stepNumber: number;
    text: string;
}

export interface Recipe {
    id: number;
    name: string;
    description: string;
    category: string;
    nutritionalContent: {
        calories: number;
        fat: string;
        saturatedFat: string;
        cholesterol: string;
        sodium: string;
        carbohydrate: string;
        fiber: string;
        sugar: string;
        protein: string;
    };
    portionFacts: {
        servings: string;
        yield: string;
    };
    timeFacts: {
        prepTime: string;
        cookTime: string;
        totalTime: string;
    };
    author: {
        username: string;
    };
    images: Array<RecipeImage>;
    ingredients: Array<RecipeIngredient>;
    instructions: Array<RecipeInstruction>;
}
