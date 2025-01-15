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
import { login } from "@/api/endpoints";
import { Separator } from "@/components/ui/separator";
import { useState } from "react";
import ReCAPTCHA from "react-google-recaptcha";
import { useTranslation } from "react-i18next";

const fallback = "/" as const;

const RECAPTCHA_KEY = import.meta.env.VITE_GOOGLE_RECAPTCHA_KEY;

export const Route = createFileRoute("/login")({
    beforeLoad: ({ context, location }) => {
        // console.log("beforeLoad", location);
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
    const { t } = useTranslation();
    return (
        <div className="flex flex-1 justify-center items-center w-full">
            <Card className="w-96">
                <CardHeader>
                    <CardTitle>{t("login")}</CardTitle>
                </CardHeader>
                <CardContent>
                    <FormContainer />
                </CardContent>

                <CardContent className="flex relative">
                    <Separator />
                    <div className="absolute w-full translate-x-1/2 -left-1/2 -top-1/2 text-center">
                        {t("or")}
                    </div>
                </CardContent>
                <CardFooter>
                    <Link className="w-full" to="/register">
                        <Button variant="ghost" className="w-full">
                            {t("sign_up")}
                        </Button>
                    </Link>
                </CardFooter>
            </Card>
        </div>
    );
}

const formSchema = z.object({
    usernameOrEmail: z.string().nonempty("Username is required"),
    password: z.string().nonempty("Password is required"),
});

const FormContainer = () => {
    const [error, setError] = useState<string | null>(null);
    const auth = useAuth();
    const router = useRouter();
    const navigate = useNavigate();
    const [captcha, setCaptcha] = useState<string>("");
    const searchParams = new URLSearchParams(window.location.search);
    const redirectTo = searchParams.get("redirect") || fallback;
    const { t } = useTranslation();
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            usernameOrEmail: "",
            password: "",
        },
    });

    async function onSubmit(values: z.infer<typeof formSchema>) {
        const { usernameOrEmail, password } = values;
        if (!usernameOrEmail || !password) return;
        try {
            const response = await login({ usernameOrEmail, password });

            auth.login(response);
            await router.invalidate();
            await sleep(100);
            await navigate({ to: redirectTo });
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
                    name="usernameOrEmail"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>{t("email")}</FormLabel>
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
                            <FormLabel>{t("password")}</FormLabel>
                            <FormControl>
                                <Input type="password" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <ReCAPTCHA
                    className="w-full"
                    sitekey={RECAPTCHA_KEY}
                    onChange={(val) => setCaptcha(val)}
                />
                {error && <FormMessage typeof="error">{error}</FormMessage>}
                <Button
                    disabled={!captcha}
                    variant="secondary"
                    type="submit"
                    className="w-full mt-4"
                >
                    {t("login")}
                </Button>
            </form>
        </Form>
    );
};
