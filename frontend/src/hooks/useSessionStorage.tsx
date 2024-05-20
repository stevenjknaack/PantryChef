import { useState, useEffect } from 'react';

export default function useSessionStorage(
    storageKey: string,
    initialValue: any
) {
    const savedData = JSON.parse(sessionStorage.getItem(storageKey) ?? 'null');

    const [data, setData] = useState(savedData ?? initialValue);

    useEffect(() => {
        sessionStorage.setItem(storageKey, JSON.stringify(data));
    }, [data]);

    return [data, setData];
}
