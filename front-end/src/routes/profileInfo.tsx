import {
    createFileRoute,
    Link,
    redirect,
    useNavigate,
} from "@tanstack/react-router";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
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
import { useState } from "react";
import { Separator } from "@/components/ui/separator";
import { updateProfile } from "@/api/endpoints";

export const Route = createFileRoute("/profileInfo")({
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
                    <CardTitle>Give more information about you</CardTitle>
                </CardHeader>
                <CardContent>
                    <FormContainer />
                </CardContent>
            </Card>
        </div>
    );
}

const formSchema = z.object({
    fullName: z.string().min(4),
    profileImg: z.string(),
    bio: z.string(),
});

const FormContainer = () => {
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();
    const navigate = useNavigate();
    const auth = useAuth();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            fullName: "",
            profileImg: "",
            bio: "",
        },
    });

    async function onSubmit(values: z.infer<typeof formSchema>) {
        const { fullName, bio, profileImg } = values;
        const username = auth.user?.username || "";
        try {
            const response = await updateProfile({
                username,
                fullName,
                bio,
                profileImg,
            });
            console.log(response);
            await router.invalidate();
            await sleep(100);
            await navigate({ to: "/" });
        } catch (error) {
            console.log(error.message);
            setError(error.message);
        }
    }

    return (
        <Form {...form}>
            <form
                onSubmit={form.handleSubmit(onSubmit)}
                className="flex flex-col gap-4"
            >
                <FormField
                    control={form.control}
                    name="fullName"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Full name</FormLabel>
                            <FormControl>
                                <Input {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="profileImg"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Profile img</FormLabel>
                            <FormControl>
                                <Input {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="bio"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Bio</FormLabel>
                            <FormControl>
                                <Input {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                {error && <FormMessage type="error">{error}</FormMessage>}
                <div className="flex justify-between h-10 items-center">
                    <Button
                        variant="ghost"
                        type="submit"
                        onClick={() => navigate({ to: "/" })}
                    >
                        Skip
                    </Button>
                    <Separator orientation="vertical" />
                    <Button variant="secondary" type="submit">
                        Sign up
                    </Button>
                </div>
            </form>
        </Form>
    );
};
