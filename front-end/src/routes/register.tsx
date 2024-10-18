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
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "@tanstack/react-router";
import { useAuth } from "../../providers/AuthProvider";
import { login, register } from "@/api/endpoints";
import { useState } from "react";

const fallback = "/" as const;

export const Route = createFileRoute("/register")({
    beforeLoad: ({ context, location }) => {
        console.log("beforeLoad", location);
        // @ts-expect-error - auth is not in the types
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
    email: z.string().email("Invalid email address"),
    username: z.string().nonempty("Username is required"),
    password: z.string().nonempty("Password is required"),
});

const FormContainer = () => {
    const [error, setError] = useState<string | null>(null);
    const auth = useAuth();
    const router = useRouter();
    const navigate = useNavigate();

    const searchParams = new URLSearchParams(window.location.search);
    const redirectTo = searchParams.get("redirect") || fallback;

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            email: "",
            username: "",
            password: "",
        },
    });

    async function onSubmit(values: z.infer<typeof formSchema>) {
        const { email, username, password } = values;
        console.log(fallback);
        if (!email || !username || !password) return;
        try {
            const response = await register({ email, username, password });
            console.log(response);
            auth.login(response);
            await router.invalidate();
        } catch (error) {
            console.log(error.message);
            setError(error.message);
        }
        // await sleep(100);
        // await navigate({ to: redirectTo });
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
                    Login
                </Button>
            </form>
        </Form>
    );
};
