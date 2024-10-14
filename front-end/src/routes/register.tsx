import { createFileRoute, redirect, useNavigate } from "@tanstack/react-router";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { useForm } from "react-hook-form";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { useState } from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "@tanstack/react-router";
import { useAuth } from "../../providers/AuthProvider";

const fallback = "/" as const;

export const Route = createFileRoute("/register")({
    beforeLoad: ({ context, location }) => {
        console.log("beforeLoad", location);
        if (context?.auth.isAuthenticated) {
            throw redirect({ to: fallback });
        }
    },
    component: Login,
});

async function sleep(ms: number) {
    return new Promise((resolve) => setTimeout(resolve, ms));
}

function Login() {
    return (
        <div className="flex flex-1 justify-center items-center w-full">
            <Card className="w-80">
                <CardHeader>
                    <CardTitle>Registration</CardTitle>
                </CardHeader>
                <CardContent>
                    <FormContainer />
                </CardContent>
            </Card>
        </div>
    );
}

const formSchema = z.object({
    email: z.string().email("Invalid email").nonempty("Email is required"),
    username: z.string().min(3, "Username must be at least 3 characters"),
    password: z.string().min(8, "Password must be at least 8 characters"),
});

const FormContainer = () => {
    const [error, setError] = useState<string | null>(null);
    const auth = useAuth();
    const router = useRouter();
    const navigate = useNavigate();

    const searchParams = new URLSearchParams(window.location.search);
    const redirectTo = searchParams.get("redirect") || fallback;

    console.log(redirectTo);
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            email: "",
            username: "",
            password: "",
        },
    });

    // 2. Define a submit handler.
    function onSubmit(values: z.infer<typeof formSchema>) {
        const { email, username, password } = values;
        console.log(fallback);
        fetch("http://localhost:3000/users/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, username, password }),
        })
            .then((response) => response.json())
            .then(async (data) => {
                console.log(data);
                if (data.statusCode === 401) {
                    setError("Invalid email or password.");
                    return;
                }
                auth.login(data);
                await router.invalidate();
                await sleep(100);
                await navigate({ to: redirectTo });
                console.log(auth.user?.email);
            })
            .catch((error) => console.error(error));
    }

    return (
        <Form {...form}>
            <form
                onSubmit={form.handleSubmit(onSubmit)}
                className="flex flex-col gap-4"
            >
                <FormField
                    control={form.control}
                    name="email"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Email</FormLabel>
                            <FormControl>
                                <Input {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="username"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Username</FormLabel>
                            <FormControl>
                                <Input {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="password"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Password</FormLabel>
                            <FormControl>
                                <Input type="password" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                {error && <FormMessage type="error">{error}</FormMessage>}
                <Button type="submit" className="w-full mt-4">
                    Sign up
                </Button>
            </form>
        </Form>
    );
};
