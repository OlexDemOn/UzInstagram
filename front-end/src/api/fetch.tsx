export async function fetchClient(
    url: string,
    method: "POST" | "GET" | "DELETE" | "PUT",
    body?: object | null,
    userName?: string
) {
    const response = await fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            Authorization: `${userName}`,
        },

        body: body && JSON.stringify(body),
    });

    const data = await response.json(); // Parse JSON response

    if (!response.ok) {
        console.error("Error:", data.message);
        throw new Error(data.message);
    }

    console.log("Success:", data.data);
    return data.data;
}
